import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:image/image.dart';
import 'package:uuid/uuid.dart';
import 'package:uuid/uuid_util.dart';
import 'dtos.dart';

Uuid uuid = new Uuid();
String USER_ID = uuid.v4();
const API_BASE_URL = "https://this-or-that-api.azurewebsites.net/this-or-that";

class ApiService {
  static Future<DecisionSet> fetchNewDecisionSet(String surveyId) async {
    String url = API_BASE_URL + "/" + surveyId + "/vote";
    final response = await http.get(url, headers: {
      "userId": USER_ID,
    });

    print("=>" + response.body);

    if (response.statusCode == 200) {
      return DecisionSet.fromJson(json.decode(response.body));
    } else {
      throw Exception("Error");
    }
  }

  static Future<ScoreSummary> fetchScoreData(String surveyId) async {
    String url = API_BASE_URL + "/" + surveyId + "/score";
    final response = await http.get(url);

    print("=>" + response.body);

    if (response.statusCode == 200) {
      return ScoreSummary.fromJson(json.decode(response.body));
    } else {
      throw Exception("Error");
    }
  }

  static Future<void> postDecisionChoice(
      String surveyId, DecisionChoice choice) async {
    String url = API_BASE_URL + "/" + surveyId + "/vote";

    String body = json.encode(choice.toMap());
    print("URL: " + url);
    print("POST-BODY: " + body);

    final response = await http.post(
      url,
      body: body,
      headers: {
        "Content-Type": "application/json",
        "userId": USER_ID,
      },
    );
    if (response.statusCode != 200) {
      throw new Exception("Error");
    }
  }

  static String buildImageUrl(String surveyId, String imageId) {
    return API_BASE_URL + "/" + surveyId + "/image/" + imageId;
  }

  static Future<CreateSurveyResponse> postSurvey(String perspective) async {
    String url = API_BASE_URL + "/create";

    var map = new Map<String, dynamic>();
    map["perspective"] = perspective;
    String body = json.encode(map);
    print("URL: " + url);
    print("POST-BODY: " + body);

    final response = await http.post(
      url,
      body: body,
      headers: {"Content-Type": "application/json"},
    );

    if (response.statusCode == 200) {
      return CreateSurveyResponse.fromJson(json.decode(response.body));
    }

    return null;
  }

  static Future<void> postImage(String code, File file) async {
    print("decodeImage");
    Image image = decodeImage(file.readAsBytesSync());
    print("copyResize");
    Image thumbnail = copyResize(image, width: 1024);

    print("encodeJpg");
    List<int> imageBytes = encodeJpg(thumbnail);

    print("base64Encode");
    String base64Image = base64Encode(imageBytes);
    var map = new Map<String, dynamic>();
    map["file"] = "data:image/jpg;base64," + base64Image;
    String body = json.encode(map);

    String url = API_BASE_URL + "/" + code + "/image";
    print("URL: " + url);
    final response = await http.post(
      url,
      body: body,
      headers: {"Content-Type": "application/json"},
    );

    if (response.statusCode == 200) {
      return CreateSurveyResponse.fromJson(json.decode(response.body));
    }

    return null;
  }

  static Future<void> startSurvey(String code) async {
    String url = API_BASE_URL + "/" + code + "/start";

    print("URL: " + url);

    final response = await http.post(url);

    if (response.statusCode == 200) {
      return null;
    }

    return null;
  }
}

class CreateSurveyResponse {
  final String perspective;

  CreateSurveyResponse({this.perspective});

  factory CreateSurveyResponse.fromJson(Map<String, dynamic> json) {
    return CreateSurveyResponse(
      perspective: json['code'],
    );
  }
}
