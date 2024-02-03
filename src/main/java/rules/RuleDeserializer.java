package rules;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RuleDeserializer implements JsonDeserializer<GameRule>, JsonSerializer<GameRule>{
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
