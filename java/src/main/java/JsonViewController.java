import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.awt.Container;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class JsonViewController {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    JsonView view;

    public JsonViewController() {
        this.view = new JsonView(this);
    }

    public void startFrom(Container container) {
        view.attachTo(container);
    }

    public void didClickLoadButton(String sURL) {
        try {
            URL url = new URL(sURL);
            URLConnection request = url.openConnection();
            request.connect();

            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            String prettyJsonString = gson.toJson(root);

            view.showJSON(prettyJsonString);
        } catch (IOException e) {
            view.showError(e.getLocalizedMessage());
        }
    }

    public boolean isValidJSON(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void didClickSaveButton(String json, File file) {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    view.showError("File " + file.getAbsolutePath() + " creation error");
                    return;
                }
            } catch (IOException e) {
                view.showError(e.getLocalizedMessage());
                return;
            }
        }

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            System.out.println(json);
            output.write(json);
            output.flush();
            output.close();
        } catch (IOException e) {
            view.showError(e.getLocalizedMessage());
            return;
        }

        view.showSuccess("Saved to file" + file.getAbsolutePath());
    }
}
