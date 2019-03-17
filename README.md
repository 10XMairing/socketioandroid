# A very simple socket.io & android implementation
A simple app and node server that connects with socket io library. Essentially the android and website can communicate with each other using sockets.

to run server
```
npm run dev
```
or
```
node index.js
```

In Android's MainActivity update the **serverurl** after hosting it with node. lookup how to connect android with localhost. I use [ngrok](https://ngrok.com/download) for ubuntu.

in linux use this command to host your localhost at port 3000 at a random url(see terminal) 
```
ngrok http 3000
```

You can also connect to your localhost by connecting your android with the same wifi with the computer and use the ip address:port


libraries used

#### android
```
implementation 'com.github.nkzawa:socket.io-client:0.3.0'
```

#### node

* socket.io
* express

Code referenced from [socket.io chat](https://socket.io/get-started/chat/)
