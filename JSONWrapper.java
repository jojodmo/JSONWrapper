/*
   Copyright 2016 Domenico Ottolia

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONWrapper{

    //Initialization
    public JSONObject json;
    public List<Object> list;
    public Object value = null;

    public JSONWrapper(){
        this.json = null;
    }

    public JSONWrapper(String value){
        if(value == null){this.json = null;}

        try{this.json = new JSONObject(value);}
        catch(Exception ex){this.json = null;}
    }

    public JSONWrapper(List<Object> list){
        this.list = list;
        this.value = list;
    }

    public JSONWrapper(JSONObject unwrapped){this.json = unwrapped;}
    public JSONWrapper(Object object){this.value = object;}

    //NotNull
    public JSONWrapper get(String key){
        if(this.json != null){
            JSONWrapper clone = this.clone();
            clone.value = this.json.opt(key);
            clone.json = this.json.optJSONObject(key);

            if(clone.value instanceof JSONArray){
                clone.list = this.JSONArrayToList((JSONArray) clone.value);
            }
            return clone;
        }

        return new JSONWrapper();
    }

    //NotNull
    public JSONWrapper get(int index){
        if(this.list != null){
            JSONWrapper clone = this.clone();
            Object obj = this.list.size() <= index ? null : this.list.get(index);

            if(obj instanceof JSONObject){clone.json = (JSONObject) obj;}
            else if(obj instanceof JSONArray){clone.list = clone.JSONArrayToList((JSONArray) obj);}
            else{clone.value = obj;}
            return clone;
        }
        JSONWrapper array = this.asArrayNullable();

        if(array != null){return array.get(index);}
        else{return new JSONWrapper();}
    }

    public List<JSONWrapper> iterator(){
        List<JSONWrapper> list = new ArrayList<>();

        for(Object obj : this.list == null ? new ArrayList<>() : this.list){
            if(obj instanceof JSONObject){list.add(new JSONWrapper((JSONObject) obj));}
            else if(obj instanceof JSONArray){list.add(new JSONWrapper(this.JSONArrayToList((JSONArray) obj)));}
            else{list.add(new JSONWrapper(obj));}
        }

        return list;
    }

    public String asString(){return this.asString(null);}
    public String asString(String defaultValue){
        if(this.value instanceof String){return (String) this.value;}
        return defaultValue;
    }

    public Boolean asBoolean(){return this.asBoolean(null);}
    public Boolean asBoolean(Boolean defaultValue){
        if(this.value instanceof Boolean){return (Boolean) this.value;}
        return defaultValue;
    }

    public Integer asInteger(){return this.asInteger(null);}
    public Integer asInteger(Integer defaultValue){
        if(this.value instanceof Integer){return (Integer) this.value;}
        return defaultValue;
    }
    
    public Float asFloat(){return this.asFloat(null);}
    public Float asFloat(Float defaultValue){
        if(this.value instanceof Float){return (Float) this.value;}
        return defaultValue;
    }

    public String[] asStringArray(){return this.asArray(new String[]{}, String.class);}
    public List<String> asStringList(){return this.asStringList(null);}
    public List<String> asStringList(List<String> defaultValue){return this.asList(defaultValue, String.class);}

    public <T> T[] asArray(T[] defaultValue, Class<T> type){
        List<T> list = this.asList(new ArrayList<T>(), type);
        return list.toArray(defaultValue);
    }
    public <E> List<E> asList(List<E> defaultValue, Class<E> type){
        if(this.value instanceof List){
            List<E> values = new ArrayList<>();
            for(Object obj : (List) this.value){
                if(type.isInstance(obj)){values.add(type.cast(obj));}
            }

            return values;
        }
        else if(this.value instanceof JSONArray){
            return this.asArray().asList(defaultValue, type);
        }
        return defaultValue;
    }

    public /*@Nullable*/ JSONWrapper asArrayNullable(){return this.asArray(null);}
    public /*@NonNull*/ JSONWrapper asArray(){return this.asArray(this);}
    public JSONWrapper asArray(JSONWrapper defaultValue){
        if(this.value instanceof JSONArray){
            JSONArray array = (JSONArray) this.value;
            return new JSONWrapper(this.JSONArrayToList(array));
        }
        return defaultValue;
    }

    private List<Object> JSONArrayToList(JSONArray array){
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < array.length(); i++){
            try{
                Object value = array.get(i);
                if(value instanceof JSONArray){value = this.JSONArrayToList((JSONArray) value);}
                list.add(value);
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        return list;
    }

    public JSONWrapper clone(){
        JSONWrapper json = new JSONWrapper();
        json.json = this.json;
        json.list = this.list;
        json.value = this.value;

        return json;
    }
}
