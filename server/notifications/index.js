// AAAAjKvbtDQ:APA91bFIZSkif2bJ-Up1dNJjBwLjRKV0R9WYyM2l6HCX_aT1UspNbigWfcDrxRhod7pY3eihN19fE2l0nEewfOdCwsqIhT0T8mwp4Whm0aBHXUKFD7tpN4uW3Dz59N6SkaPmHhDBrFAQ


var fcm = require('fcm-notification');
var FCM = new fcm('./k.json');
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
 



///
var admin = require("firebase-admin");

var serviceAccount = require("./k.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: ""
});


var db = admin.database();
var ref = db.ref("poll_detail");

// Attach an asynchronous callback to read the data at our posts reference
ref.once("value", function(snapshot) {
    var keys = Object.keys(snapshot.val()||{});
    var lastIdInSnapshot = keys[keys.length-1]

    ref.orderByKey().startAt(lastIdInSnapshot).on("child_added", function(newMessSnapshot) {
        if( newMessSnapshot.key === lastIdInSnapshot ) { return; }
        console.log('new record', newMessSnapshot.key);
        FCM.send(message, function(err, response) {
            if(err){
                console.log('error found', err);
            }else {
                console.log('response here', response);
            }
        })
    })


    // ref.on("child_added", function(snapshot) {
    //     if(snapshot.getMetadata().isFromCache()) {console.log("CASCH")}
    //     //console.log(snapshot.val());
    //     counter++;
    //     if (counter < old_data_count) { return; }

    //     FCM.send(message, function(err, response) {
    //         if(err){
    //             console.log('error found', err);
    //         }else {
    //             console.log('response here', response);
    //         }
    //     })
    // })
}, function (errorObject) {
  console.log("The read failed: " + errorObject.code);
});