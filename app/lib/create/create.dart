import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:this_or_that_app/create/loading.dart';
import 'package:this_or_that_app/create/success.dart';
import 'package:this_or_that_app/service/api-service.dart';
import 'package:this_or_that_app/utlis/image.util.dart';

import 'failed.dart';
import 'image.dart';

class CreateScreen extends StatefulWidget {
  @override
  CreateScreenState createState() => CreateScreenState();
}

class CreateScreenState extends State<CreateScreen> {
  var txtId = TextEditingController();

  List<File> _files = [];
  List<Widget> _images = [];
  String _loadingText = "";
  String _code = "";
  String _title = "";
  bool _hasSucceeded = false;
  bool _hasFailed = false;
  bool _isLoading = false;

  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    if (this._hasFailed) {
      return FailedWidget();
    }
    if (this._isLoading) {
      return LoadingWidget(
        title: _title,
        message: _loadingText,
      );
    }
    if (this._hasSucceeded) {
      return SuccessWidget(
        code: _code,
        title: _title,
      );
    }
    return Scaffold(
      appBar: AppBar(
        title: Text("Create Survey"),
        actions: <Widget>[],
      ),
      body: SafeArea(
          child: Form(
        key: _formKey,
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
        ),
      )),
      floatingActionButton: FloatingActionButton.extended(
        backgroundColor: Colors.tealAccent[400],
        foregroundColor: Colors.white,
        onPressed: () {
          if (_formKey.currentState.validate()) {
            createSurvey();
          }
        },
        tooltip: 'Save survey',
        label: Text("Save survey"),
        icon: Icon(Icons.save),
      ),
    );
  }

  Future createSurvey() async {
    setState(() {
      _isLoading = true;
      _loadingText = "Start creating the survey";
    });
    try {
      CreateSurveyResponse response = await ApiService.postSurvey(txtId.text);
      String code = response.perspective;
      setState(() {
        _code = code;
        _loadingText = "Survey created. Start resizing the images";
      });

      List<String> listOfBase64DataUris = await Future.wait(
          _files.map((file) => ImageUtil.convertImages(code, file)));
      setState(() {
        _loadingText = "Uploading images";
      });

      await Future.wait(listOfBase64DataUris
          .map((base64DataUri) => ApiService.postImage(code, base64DataUri)));

      setState(() {
        _loadingText = "Finished the images uploade.";
      });

      await ApiService.startSurvey(code);
      setState(() {
        _title = txtId.text;
        _isLoading = false;
        _loadingText = "";
        _hasSucceeded = true;
      });
    } catch (e) {
      setState(() {
        _hasFailed = true;
      });
    }
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
