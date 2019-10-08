import 'package:flutter/material.dart';
import 'package:barcode_scan/barcode_scan.dart';
import 'package:flutter/services.dart';

import 'common/logo.dart';
import 'common/or_divider.dart';
import 'vote.dart';

const String VOTE_STRING = "/vote/";
const String URL_START = "http://";

class StartScreen extends StatefulWidget {
  StartScreen({Key key, this.title}) : super(key: key);

  final String title;

  @override
  StartScreenState createState() => StartScreenState();
}

class StartScreenState extends State<StartScreen> {
  String barcode;
  var txtId = TextEditingController();

  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blueAccent[700],
        title: Text("This or That"),
      ),
      body: Container(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(20, 0, 20, 10),
          child: Form(
            key: _formKey,
            child: Column(
              children: <Widget>[
                LogoImageWidget(),
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
                  child: TextFormField(
                    controller: txtId,
                      validator: (value) {
                        if (value.isEmpty) {
                          return 'Please enter some text';
                        }
                        return null;
                      },
                      decoration: InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Enter survey code',
                      )),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 10, 0, 10),
                  child: MaterialButton(
                    height: 60,
                    onPressed: () {
                      if (_formKey.currentState.validate()) {
                        openVote();
                      }
                    },
                    child: Text("Let's vote", style: TextStyle(fontSize: 20)),
                    color: Colors.blueAccent[700],
                    textColor: Colors.white,
                    splashColor: Colors.white,
                    disabledColor: Colors.grey[300],
                    minWidth: double.infinity,
                  ),
                ),
                OrDividerWidget(),
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 10, 0, 10),
                  child: MaterialButton(
                    height: 60,
                    onPressed: scan,
                    child: Text("Scan survey QR-code",
                        style: TextStyle(fontSize: 20)),
                    color: Colors.blueAccent[700],
                    textColor: Colors.white,
                    splashColor: Colors.white,
                    minWidth: double.infinity,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
      // floatingActionButton: FloatingActionButton(
      //   backgroundColor: Colors.tealAccent[400],
      //   onPressed: createNewSurvey,
      //   tooltip: 'Create a new survey',
      //   child: const Icon(Icons.add),
      // ),
    );
  }

  void createNewSurvey() {}

  void openVote() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => VoteScreen(surveyCode: this.txtId.text)));
  }

  Future scan() async {
    try {
      String barcode = await BarcodeScanner.scan();
      setState(() => this.txtId.text = cleanBarcode(barcode));
      openVote();
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

  String cleanBarcode(String url) {
    if (url.startsWith(URL_START)) {
      return url.substring(url.indexOf(VOTE_STRING) + VOTE_STRING.length);
    }
    return url;
  }
}
