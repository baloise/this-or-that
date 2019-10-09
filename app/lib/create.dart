import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

class CreateScreen extends StatefulWidget {
  @override
  CreateScreenState createState() => CreateScreenState();
}

class CreateScreenState extends State<CreateScreen> {
  var txtId = TextEditingController();
  var _files = const <File>[];
  var _images = const <Widget>[];

  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Create Survey"),
          actions: <Widget>[],
        ),
        body: SafeArea(
            child: Column(
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 10),
              child: TextFormField(
                  controller: txtId,
                  validator: (value) {
                    if (value.isEmpty) {
                      return 'Please enter a survey title';
                    }
                    return null;
                  },
                  decoration: InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: 'Enter survey title',
                  )),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
              child: MaterialButton(
                height: 60,
                onPressed: choose,
                child: Text("Add images", style: TextStyle(fontSize: 20)),
                color: Colors.blueAccent[700],
                textColor: Colors.white,
                splashColor: Colors.white,
                minWidth: double.infinity,
              ),
            ),
            Expanded(
              child: GridView.count(
                primary: false,
                padding: const EdgeInsets.all(20),
                crossAxisSpacing: 10,
                mainAxisSpacing: 10,
                crossAxisCount: 3,
                children: _images == null ? [] : _images,
              ),
            )
          ],
        )));
  }

  Future choose() async {
    // File file = await ImagePicker.pickImage(source: ImageSource.camera);
    // _files.add(file);
    print('???????????????????????');
    print('choose');
    _images.add(ImageWidget());
    print(_images.length);

    setState(() {
      _files = _files;
      _images = _images;
    });
  }
}

class ImageWidget extends StatelessWidget {
  final File file;

  ImageWidget({this.file});

  @override
  Widget build(BuildContext context) {
    AssetImage logoAsset = AssetImage('images/logo.png');
    Image image = Image(image: logoAsset, width: 150.0, height: 150.0);
    return Container(
      child: image,
    );
  }
}
