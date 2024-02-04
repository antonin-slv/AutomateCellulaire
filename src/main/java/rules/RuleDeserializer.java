package rules;

import com.google.gson.*;

import java.lang.reflect.Type;
/**
 * Class that allows to serialize and deserialize a GameRule from and to a json.
 * @see rules.GameRule
 * @see com.google.gson.JsonDeserializer
 * @see com.google.gson.JsonSerializer
 */
public class RuleDeserializer implements JsonDeserializer<GameRule>, JsonSerializer<GameRule>{
    /**
     * Function that deserialize a json to a GameRule.
     * It gives this gameRule the right type to work perfectly.
     * @param jsonElement the json to deserialize
     * @param type the type of the object to deserialize (unused)
     * @param ctx the context of the deserialization
     * @return the deserialized GameRule
     * @throws JsonParseException if the json is not valid
     * @see com.google.gson.JsonElement
     * @see com.google.gson.JsonDeserializationContext
     */
    @Override
    public GameRule deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject root = jsonElement.getAsJsonObject();
        String name = root.get("name").getAsString();

        Class<? extends GameRule> ruleClass = switch (name) {
            case "somme" -> SumRule.class;
            case "table" -> TabRule.class;
            case "majority" -> MajRule.class;
            case "average" -> AvgRule.class;
            case "direction" -> DirRule.class;
            default -> throw new UnsupportedOperationException("Unknown rule `" + name + "`");
        };

        return ctx.deserialize(root, ruleClass);
    }

    /**
     * Function that serialize a GameRule to a json.
     * It gives this json the right type to work perfectly.
     * @param src the GameRule to serialize
     * @param typeOfSrc the type of the object to serialize (unused)
     * @param context the context of the serialization
     * @return the serialized json
     * @see com.google.gson.JsonElement
     */
    @Override
    public JsonElement serialize(GameRule src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = context.serialize(src).getAsJsonObject();
        if (src instanceof SumRule) {
            root.addProperty("name", "somme");
        } else if (src instanceof TabRule) {
            root.addProperty("name", "table");
        } else if (src instanceof MajRule) {
            root.addProperty("name", "majority");
        } else if (src instanceof AvgRule) {
            root.addProperty("name", "average");
        } else if (src instanceof DirRule) {
            root.addProperty("name", "direction");
        } else {
            throw new UnsupportedOperationException("Unknown rule `" + src.getClass().getSimpleName() + "`");
        }
        return root;
    }
}
