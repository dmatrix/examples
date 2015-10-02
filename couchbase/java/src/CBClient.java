import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

/**
 * Created by jdamji on 10/1/15.
 */
public class CBClient {

    public static void main(String[] args) {
        //By default without arguments it connects to localhost
        CouchbaseCluster cluster = CouchbaseCluster.create();
        Bucket bucket = cluster.openBucket();
        // let's create a json document
        JsonObject user = JsonObject.empty()
                .put("firstname", "Jules")
                .put("lastname", "Damji")
                .put("job", "Develoepr Advocate")
                .put("age", 102);
        //now we can do a simple insert in the default bucket
        JsonDocument doc = JsonDocument.create("jules", user);
        JsonDocument response = bucket.upsert(doc);

        //now let's try to retrieve it.
        JsonDocument julesDoc = bucket.get("jules");
        System.out.println(String.format("Json Document retrieved: %s", julesDoc));
    }
}
