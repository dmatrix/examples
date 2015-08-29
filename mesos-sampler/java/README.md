# Java Sampler Framework

1. `mvn clean compile assembly:single`
1. `export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib`
1. `java -cp target/sampler-1.0-SNAPSHOT-jar-with-dependencies.jar com.mesosphere.sampler.main.SamplerMain 127.0.1.1:5050 5 `
