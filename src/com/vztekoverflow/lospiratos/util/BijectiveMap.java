package com.vztekoverflow.lospiratos.util;

import java.util.HashMap;
import java.util.Map;


//this code has been copies from StackOverflow, https://stackoverflow.com/questions/1670038/does-java-have-a-hashmap-with-reverse-lookup (with few modifications)
public class BijectiveMap<KeyType, ValueType> {

    private Map<KeyType, ValueType> keyToValueMap = new HashMap<KeyType, ValueType>();
    private Map<ValueType, KeyType> valueToKeyMap = new HashMap<ValueType, KeyType>();

    public void put(KeyType key, ValueType value){
        keyToValueMap.put(key, value);
        valueToKeyMap.put(value, key);
    }

    public ValueType removeByKey(KeyType key){
        ValueType removedValue = keyToValueMap.remove(key);
        valueToKeyMap.remove(removedValue);
        return removedValue;
    }

    public KeyType removeByValue(ValueType value){
        KeyType removedKey = valueToKeyMap.remove(value);
        keyToValueMap.remove(removedKey);
        return removedKey;
    }

    public boolean containsKey(KeyType key){
        return keyToValueMap.containsKey(key);
    }

    public boolean containsValue(ValueType value){
        return valueToKeyMap.containsKey(value);
    }

    public KeyType getKey(ValueType value){
        return valueToKeyMap.get(value);
    }

    public ValueType getValue(KeyType key){
        return keyToValueMap.get(key);
    }
}
