import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:this_or_that_app/service/api-service.dart';

class CreateScreen extends StatefulWidget {
  @override
  CreateScreenState createState() => CreateScreenState();
}

class CreateScreenState extends State<CreateScreen> {
  var txtId = TextEditingController();
  List<File> _files = [];
  List<Widget> _images = [];

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
      )),
      floatingActionButton: FloatingActionButton(
        backgroundColor: Colors.tealAccent[400],
        foregroundColor: Colors.white,
        onPressed: createSurvey,
        tooltip: 'Create survey',
        child: const Icon(Icons.save),
      ),
    );
  }

  Future createSurvey() async {
    CreateSurveyResponse response = await ApiService.postSurvey(txtId.text);
    print('!!!!!!!!!!!!!!!!');
    print('!!!!!!!!!!!!!!!!');
    print('!!!!!!!!!!!!!!!!');
    print(response.perspective);
  }

  Future choose() async {
    File file = await ImagePicker.pickImage(source: ImageSource.camera);

    List<Widget> images = [];
    images.addAll(this._images);
    images.add(ImageWidget(
      file: file,
    ));

    List<File> files = [];
    files.addAll(this._files);
    files.add(file);

    setState(() {
      _files = files;
      _images = images;
    });
  }
}

class ImageWidget extends StatelessWidget {
  final File file;

  ImageWidget({this.file});

  @override
  Widget build(BuildContext context) {
    if (this.file != null) {
      return Container(
        child: Image.file(file),
      );
    }
    return Container();
  }
}
