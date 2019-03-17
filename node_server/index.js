const app = require("express")();
const http = require("http").Server(app);
const io = require("socket.io")(http);

app.get("/", (req, res) => {
  res.sendFile(__dirname + "/index.html");
});

io.on("connection", socket => {
  console.log("connected");
  socket.off("chat message", msg => {
    console.log("Disconnected");
  });

  socket.on("chat message", msg => {
    console.log(msg);
    io.emit("chat message", msg);
  });
});

http.listen(3000, () => {
  console.log("listening on port 3000");
});
