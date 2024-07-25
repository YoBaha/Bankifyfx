package controllers.Cheques;

import models.Cheques.Reclamation;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class typeadapter extends TypeAdapter<Reclamation> {

    @Override
    public void write(JsonWriter out, Reclamation value) throws IOException {
        out.beginObject();
        out.name("id").value(value.getId());
        out.name("chequeId").value(value.getChequeId());
        out.name("categorie").value(value.getCategorie());
        out.name("statutR").value(value.getStatutR());
        out.endObject();
    }


    @Override
    public Reclamation read(JsonReader in) throws IOException {
        Reclamation reclamation = new Reclamation();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    reclamation.setId(in.nextInt());
                    break;
                case "chequeId":
                    reclamation.setChequeId(in.nextInt());
                    break;
                case "categorie":
                    reclamation.setCategorie(in.nextString());
                    break;
                case "statutR":
                    reclamation.setStatutR(in.nextString());
                    break;
                default:
                    in.skipValue(); // skip values of unknown keys
                    break;
            }
        }
        in.endObject();
        return reclamation;
    }

}
