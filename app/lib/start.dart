import 'package:barcode_scan/barcode_scan.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:package_info/package_info.dart';
import 'package:this_or_that_app/create.dart';

import 'common/logo.dart';
import 'common/or_divider.dart';
import 'history.dart';
import 'service/local-storage-service.dart';
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
        title: Text("This or That"),
        actions: <Widget>[
          IconButton(
              icon: Icon(Icons.history),
              tooltip: 'History',
              onPressed: openHistory),
          IconButton(
              icon: Icon(Icons.info),
              tooltip: 'About this app',
              onPressed: openAbout),
        ],
      ),
      body: SafeArea(
        child: Container(
          child: Padding(
            padding: const EdgeInsets.fromLTRB(20, 0, 20, 10),
            child: Form(
              key: _formKey,
              child: ListView(
                children: <Widget>[
                  LogoImageWidget(),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
                    child: TextFormField(
                        controller: txtId,
                        validator: (value) {
                          if (value.isEmpty) {
                            return 'Please enter a valid survey code';
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
      ),
      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.tealAccent[400],
        foregroundColor: Colors.white,
        onPressed: createNewSurvey,
        tooltip: 'Create survey',
        child: const Icon(Icons.add),
      ),
    );
  }

  void createNewSurvey() {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => CreateScreen()));
  }

  void openVote() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => VoteScreen(surveyCode: this.txtId.text)));
  }

  void openHistory() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => HistoryScreen()));
  }

  void openAbout() {
    String version = "";
    String buildNumber = "";

    PackageInfo.fromPlatform().then((PackageInfo packageInfo) {
      version = packageInfo.version;
      buildNumber = packageInfo.buildNumber;
    });

    showDialog(
      context: context,
      builder: (context) => AboutDialog(
          applicationVersion: "Version: " + version + " #" + buildNumber,
          applicationLegalese: "Apache 2.0 License"),
    );
  }

  String cleanBarcode(String url) {
    if (url.startsWith(URL_START)) {
      return url.substring(url.indexOf(VOTE_STRING) + VOTE_STRING.length);
    }
    return url;
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
}
