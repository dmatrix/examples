package main.java.com.dmx.main;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

/**
 * Created by jdamji on 10/1/15.
 */

public class CBClient {

	public static void main(String[] args) {
		// By default without arguments it connects to localhost
		CouchbaseCluster cluster = CouchbaseCluster.create();

		// open the default bucket and beer-bucket
		Bucket defaultBucket = cluster.openBucket();
		Bucket beerBucket = cluster.openBucket("beer-sample");

		// let's create a json document
		JsonObject user = JsonObject.empty()
                .put("firstname", "Jules")
				.put("lastname", "Damji")
                .put("job", "Developer Advocate")
				.put("age", 102);

		// now we can do a simple insert in the default bucket
		JsonDocument doc = JsonDocument.create("jules", user);
		JsonDocument response = defaultBucket.upsert(doc);

		// now let's try to retrieve it.
		JsonDocument julesDoc = defaultBucket.get("jules");
		System.out.println(String.format("Json Document retrieved: %s",
				julesDoc));

		// now fetch from the beer-bucket
		JsonDocument beerDoc = beerBucket.get("21st_amendment_brewery_cafe");
		System.out.println(String
				.format("Json Document retrieved: %s", beerDoc));

		// close the buckets and cluster connection
		beerBucket.close();
		defaultBucket.close();
		cluster.disconnect();

	}
}
