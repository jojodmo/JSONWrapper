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

package co.getblix.blix.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSON{

    //Initialization
    public JSONObject json;
    public List<Object> list;
    public Object value = null;

    public JSON(){
        this.json = null;
    }

    public JSON(String value){
        if(value == null){this.json = null;}

        try{this.json = new JSONObject(value);}
        catch(Exception ex){this.json = null;}
    }

    public JSON(List<Object> list){
        this.list = list;
        this.value = list;
    }

    public JSON(JSONObject unwrapped){this.json = unwrapped;}
    public JSON(Object object){this.value = object;}

    public @NonNull JSON get(String key){
        JSON clone = this.clone();
        if(this.json != null){
            clone.value = this.json.opt(key);
            clone.json = this.json.optJSONObject(key);

            if(clone.value instanceof JSONArray){
                clone.list = this.JSONArrayToList((JSONArray) clone.value);
            }
        }

        return clone;
    }

    public @NonNull JSON get(int index){
        JSON clone = this.clone();
        if(this.list != null){
            Object obj = this.list.get(index);

            if(obj instanceof JSONObject){clone.json = (JSONObject) obj;}
            else if(obj instanceof JSONArray){clone.list = clone.JSONArrayToList((JSONArray) obj);}
            else{clone.value = obj;}
        }
        JSON array = this.asArrayNullable();

        if(array != null){return array.get(index);}
        else{return clone;}
    }

    public List<JSON> iterator(){
        List<JSON> list = new ArrayList<>();

        System.out.println(this.list);
        for(Object obj : this.list == null ? new ArrayList<>() : this.list){
            System.out.println(obj);
            if(obj instanceof JSONObject){list.add(new JSON((JSONObject) obj));}
            else if(obj instanceof JSONArray){list.add(new JSON(this.JSONArrayToList((JSONArray) obj)));}
            else{list.add(new JSON(obj));}
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

    public @Nullable JSON asArrayNullable(){return this.asArray(null);}
    public @NonNull JSON asArray(){return this.asArray(this);}
    public JSON asArray(JSON defaultValue){
        if(this.value instanceof JSONArray){
            JSONArray array = (JSONArray) this.value;
            return new JSON(this.JSONArrayToList(array));
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

    public JSON clone(){
        JSON json = new JSON();
        json.json = this.json;
        json.list = this.list;
        json.value = this.value;

        return json;
    }

    @Override
    public String toString(){
        String result = this.json == null ? (this.list == null ? (this.value == null ? "<null>" : this.value.toString()) : this.list.toString()) : this.json.toString();
        return "JSON(" + result + ")";
    }
}
