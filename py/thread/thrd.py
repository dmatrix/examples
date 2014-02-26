import threading
import datetime
import time

class ThreadClass(threading.Thread):
    def run(self):
        now = datetime.datetime.now()
        print "%s says Hello World at time: %s" % (self.getName(), now)

for i in range(2):
    t = ThreadClass()
    t.start()
    time.sleep(2)