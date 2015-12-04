import argparse

from influxdb import InfluxDBClient


def main(host='localhost', port=8086):
    user = 'pubnub'
    password = 'pubnub'
    dbname = 'pubnub_devices'
    dbuser = 'jules'
    dbuser_password = 'influxdb'
    query = 'select * from temperature;'
    json_body_1 = [
        {
            "measurement": "temperature",
            "tags": {
                "humidity": "35",
                "temperature": "87",
                "device_id" :"3",
                "device_name": "meter-gauge-241swMTf39L",
                "zipcode": "94538"
            },
            "time": "2009-11-10T23:00:00Z",
            "fields": {
                "lat": 45,
                "long": 55,
                "scale": "Celcius"
            }
        }
    ]
    json_body_2 = [
        {
            "measurement": "humidity",
            "tags": {
                "humidity": "35",
                "temperature": "87",
                "device_id" :"3",
                "device_name": "meter-gauge-241swMTf39L",
                "zipcode": "94538"
            },
            "time": "2009-11-10T23:00:00Z",
            "fields": {
                "lat": 45,
                "long": 55,
                "scale": "Celcius"
            }
        }
    ]

    client = InfluxDBClient(host, port, user, password, dbname)

    print("Create database: " + dbname)
    client.create_database(dbname)

    print("Create a retention policy")
    client.create_retention_policy('awesome_policy', '3d', 3, default=True)

    print("Switch user: " + dbuser)
    client.switch_user(dbuser, dbuser_password)

    print("Write points: {0}".format(json_body_1))
    client.write_points(json_body_1)

    print("Write points: {0}".format(json_body_2))
    client.write_points(json_body_2)

    print("Queying data: " + query)
    result = client.query(query)

    print("Result: {0}".format(result))

    print("Switch user: " + user)
    client.switch_user(user, password)

    print("Drop database: " + dbname)
    client.drop_database(dbname)


def parse_args():
    parser = argparse.ArgumentParser(
        description='example code to play with InfluxDB')
    parser.add_argument('--host', type=str, required=False, default='localhost',
                        help='hostname of InfluxDB http API')
    parser.add_argument('--port', type=int, required=False, default=8086,
                        help='port of InfluxDB http API')
    return parser.parse_args()


if __name__ == '__main__':
    args = parse_args()
    main(host=args.host, port=args.port)