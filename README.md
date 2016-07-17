# JSONWrapper

This is a lightweight, safe, simple wrapper around org.JSON.JSONObject that does not throw exceptions

## Usage

Take the following JSON for example

    {
      "success" : true,
      "code" : 200,
      "message" : "Success!",
      "result" : {
        "numberOfUnicorns" : 4,
        "names" : [
          "Unicorn One",
          "Unicorn Three",
          "Unicorn Tricorn",
          "Unicorn Popcorn",
        ]
      }
    }
    
A new `JSONWrapper` can be initialized using `JSONWrapper(String rawJSON)`, or using `JSONWrapper(org.json.JSONObject unwrapped)`

To go deeper into the JSON tree, use `.get(String key)`. To get the value at the current index, use the `.as` methods

    .asString(String defaultValue); //will return defaultValue if the value is not found
    .asInteger(Integer defaultValue); //will return defaultValue if the value is not found
    .asBoolean(Boolean defaultValue); //will return defaultValue if the value is not found
    .asString(); //shorthand for .asString(null);
    .asInteger(); //shorthand for .asInteger(null);
    .asBoolean(); //shorthand for .asBoolean(null);

For example, let's use `JSONWrapper wrapper` to represent the JSON above

    wrapper.get("success").asBoolean(); //true
    wrapper.get("nonExistant").asBoolean(); //null
    wrapper.get("nonExistant").asBoolean(false); //false
    wrapper.get("code").asInteger(); //200
    wrapper.get("message").asString("default value") //"Success!"
    wrapper.get("nonExistant").asString("default value") //"default value"
    
    wrapper.get("result").asBoolean(); //null
    wrapper.get("result").get("numberOfUnicorns").asInteger(); //4
    wrapper.get("nonExistant").get("nonExistant").get("stillNonExistant").asString(); //null
    
    wrapper.get("result").get("names").asString(); //null
    wrapper.get("result").get("names").asStringArray(); //["Unicorn One", "Unicorn Three", "Unicorn Tricorn", "Unicorn Popcorn"]
    wrapper.get("result").get("names").get(0).asString(); //"Unicorn One"
    wrapper.get("result").get("names").get(3).asString("Default Value"); //"Unicorn Popcorn"
    wrapper.get("result").get("names").get(4).asString("Default Value"); //"Default Value"
    
    

