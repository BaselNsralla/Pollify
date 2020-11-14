const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

var fcm = require('fcm-notification');
var FCM = new fcm('k.json');
//var token = 'token here';
 
var message = {
    data: {    //This is only optional, you can send any data
        title: 'Ny post',
        dec: '2:45'
    },
    notification:{
        title : 'Title of notification',
        body : 'Body of notification'
    },
    topic : 'poll'
};
 

// För att deploya
// $ npm install firebase-tools
// $ firebase login
// $ firebase deploy --only functions
// $ firebase functions:log

exports.sendNotification = functions.database.ref('/poll_detail')
    .onUpdate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
        //const updateValue = snapshot.after.val();
      //console.log('Uppercasing', context.params.pushId, original);
      //const uppercase = original.toUpperCase();

        //console.log("UPDATE VALUE", updateValue);
        FCM.send(message, function(err, response) {
            if(err){
                console.log('error found', err);
            } else {
                console.log('response here', response);
            }
         });
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      //return snapshot.ref.parent.child('uppercase').set(uppercase);
    });