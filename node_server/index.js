const app = require("express")();
const http = require("http").Server(app);
const io = require("socket.io")(http);

//server a html file
app.get("/", (req, res) => {
  res.sendFile(__dirname + "/index.html");
});

//create a socket connection

io.on("connection", socket => {
  console.log("connected");
  socket.off("chat message", msg => {
    console.log("Disconnected");
  });

  //do something with message recieved, in this case emit the msg so that all clients can recieve it
  socket.on("chat message", msg => {
    console.log(msg);
    io.emit("chat message", msg);
  });
});

//run server at port 3000
http.listen(3000, () => {
  console.log("listening on port 3000");
});
