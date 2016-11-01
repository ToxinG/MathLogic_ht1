/**
 * Created by Антон on 30.10.2016.
 */
public class Entity {
    Entity left, right;
    enum EntityType {impl, con, dis, neg, none};
    EntityType type = EntityType.none;
    String stringRepresentation = "";
    String unparsed = "";
}
