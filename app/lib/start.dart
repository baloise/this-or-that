import 'package:flutter/material.dart';
import 'package:barcode_scan/barcode_scan.dart';
import 'package:flutter/services.dart';

import 'admin.dart';
import 'vote.dart';

class StartScreen extends StatefulWidget {
  StartScreen({Key key, this.title}) : super(key: key);

  final String title;

  @override
  StartScreenState createState() => StartScreenState();
}

class StartScreenState extends State<StartScreen> {
  String barcode = null;
  var txtId = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('This or That'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: MaterialButton(
                onPressed: scan,
                child: Text("Scan QR code", style: TextStyle(fontSize: 20)),
                color: Theme.of(context).primaryColor,
                textColor: Colors.white,
                splashColor: Colors.white,
                minWidth: double.infinity,
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                "or"
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: TextField(
                decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Enter survey code',
                ),
                controller: txtId,
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: MaterialButton(
                onPressed: openVote,
                child: Text("Open with survey code",
                    style: TextStyle(fontSize: 20)),
                color: Theme.of(context).primaryColor,
                textColor: Colors.white,
                splashColor: Colors.white,
                minWidth: double.infinity,
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Divider(
                color: Colors.grey
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: MaterialButton(
                onPressed: openAdmin,
                child: Text("Manage surveys",
                    style: TextStyle(fontSize: 20)),
                color: Theme.of(context).primaryColor,
                textColor: Colors.white,
                splashColor: Colors.white,
                minWidth: double.infinity,
              ),
            ),
          ],
        ),
      ),
    );
  }

  void openVote() {
    Navigator.push(context, MaterialPageRoute(builder: (context) => VoteScreen(surveyCode: this.txtId.text)));
  }

  void openAdmin() {
    Navigator.push(context, MaterialPageRoute(builder: (context) => AdminScreen()));
  }

  Future scan() async {
    try {
      String barcode = await BarcodeScanner.scan();
      setState(() => this.txtId.text = barcode);
    } on PlatformException catch (e) {
      if (e.code == BarcodeScanner.CameraAccessDenied) {
        setState(() {
          this.barcode = 'The user did not grant the camera permission!';
        });
      } else {
        setState(() => this.barcode = 'Unknown error: $e');
      }
    } on FormatException {
      setState(() => this.barcode =
      'null (User returned using the "back"-button before scanning anything. Result)');
    } catch (e) {
      setState(() => this.barcode = 'Unknown error: $e');
    }
  }

}
