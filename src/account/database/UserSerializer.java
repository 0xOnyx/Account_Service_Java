package account.database;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User value, JsonGenerator jGen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
            jGen.writeStartObject();
            jGen.writeNumberField("id", value.getId());
            jGen.writeStringField("name", value.getName());
            jGen.writeStringField("lastname", value.getLastname());
            jGen.writeStringField("email", value.getEmail());
            jGen.writeArrayFieldStart("roles");
            List<Role> sortedRoles = new ArrayList<>(value.getRoles());

            sortedRoles.sort(new Role.comparatorRole());
            for (Role role : sortedRoles) {
                jGen.writeString("ROLE_" + role.getName());
            }
            jGen.writeEndArray();
            jGen.writeEndObject();


    }
}
