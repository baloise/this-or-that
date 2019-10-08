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
  String barcode;
  var txtId = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return SafeArea(
        child: Scaffold(
            appBar: null,
            body: Container(
              child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Column(
                    children: <Widget>[
                      LogoImageWidget(),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(40, 15, 40, 15),
                        child: Text(
                          'Make your prioritization',
                          style: TextStyle(
                              fontWeight: FontWeight.bold, fontSize: 25),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(40, 20, 40, 0),
                        child: TextField(
                            decoration: InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'Enter survey code',
                        )),
                      ),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(40, 20, 40, 0),
                        child: MaterialButton(
                          height: 60,
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
                        padding: const EdgeInsets.fromLTRB(40, 20, 40, 0),
                        child: Text(
                          "or",
                          style: TextStyle(fontSize: 20),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(40, 20, 40, 0),
                        child: MaterialButton(
                          height: 60,
                          onPressed: scan,
                          child: Text("Scan QR code",
                              style: TextStyle(fontSize: 20)),
                          color: Theme.of(context).primaryColor,
                          textColor: Colors.white,
                          splashColor: Colors.white,
                          minWidth: double.infinity,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(40, 40, 40, 40),
                        child: Divider(color: Colors.grey),
                      ),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(40, 20, 40, 0),
                        child: MaterialButton(
                          height: 60,
                          onPressed: openAdmin,
                          child: Text("Admin", style: TextStyle(fontSize: 20)),
                          color: Theme.of(context).primaryColor,
                          textColor: Colors.white,
                          splashColor: Colors.white,
                          minWidth: double.infinity,
                        ),
                      ),
                    ],
                  )),
            )));

    //   child: Row(
    // mainAxisAlignment: MainAxisAlignment.center,
    // children: <Widget>[
    //   Column(
    //     children: <Widget>[
    //       LogoImageWidget(),
    //       Text(
    //         'Make your prioritizationd',
    //         style: TextStyle(
    //             fontWeight: FontWeight.bold, fontSize: 25),
    //       ),
    //       Row(
    //         children: <Widget>[
    //           SizedBox(
    //               width: '100%',
    //               child: TextField(
    //                   decoration: InputDecoration(
    //                 border: OutlineInputBorder(),
    //                 labelText: 'Enter survey code',
    //               ))),
    //         ],
    //       ),
    // Container(
    // child: Flexible(
    // child: TextField(
    //   decoration: InputDecoration(
    //     border: OutlineInputBorder(),
    //     labelText: 'Enter survey code',
    //   ),
    //   controller: txtId,
    // ),
    // ),
    // ),
    //         Text('jalsfkdf')
    //       ],
    //     )
    //   ],
    // )),
    // )
    // child: Column(
    //   mainAxisAlignment: MainAxisAlignment.center,
    //   children: <Widget>[
    // Padding(
    //   padding: const EdgeInsets.all(8.0),
    //   child: MaterialButton(
    //     onPressed: scan,
    //     child: Text("Scan QR code", style: TextStyle(fontSize: 20)),
    //     color: Theme.of(context).primaryColor,
    //     textColor: Colors.white,
    //     splashColor: Colors.white,
    //     minWidth: double.infinity,
    //   ),
    // ),
    // Padding(
    //   padding: const EdgeInsets.all(8.0),
    //   child: Text("or"),
    // ),
    // Padding(
    //   padding: const EdgeInsets.all(8.0),
    //   child: TextField(
    //     decoration: InputDecoration(
    //       border: OutlineInputBorder(),
    //       labelText: 'Enter survey code',
    //     ),
    //     controller: txtId,
    //   ),
    // ),
    // Padding(
    //   padding: const EdgeInsets.all(8.0),
    //   child: MaterialButton(
    //     onPressed: openVote,
    //     child: Text("Open with survey code",
    //         style: TextStyle(fontSize: 20)),
    //     color: Theme.of(context).primaryColor,
    //     textColor: Colors.white,
    //     splashColor: Colors.white,
    //     minWidth: double.infinity,
    //   ),
    // ),
    // Padding(
    //   padding: const EdgeInsets.all(8.0),
    //   child: Divider(color: Colors.grey),
    // ),
    // Padding(
    //   padding: const EdgeInsets.all(8.0),
    //   child: MaterialButton(
    //     onPressed: openAdmin,
    //     child: Text("Manage surveys", style: TextStyle(fontSize: 20)),
    //     color: Theme.of(context).primaryColor,
    //     textColor: Colors.white,
    //     splashColor: Colors.white,
    //     minWidth: double.infinity,
    //   ),
    // ),
    //     ],
    //   ),
    // ));
  }

  void openVote() {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => VoteScreen(surveyCode: this.txtId.text)));
  }

  void openAdmin() {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => AdminScreen()));
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

class LogoImageWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    AssetImage logoAsset = AssetImage('images/logo.png');
    Image image = Image(image: logoAsset, width: 200.0, height: 200.0);
    return Container(
      child: image,
    );
  }
}
