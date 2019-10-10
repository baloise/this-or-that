import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:uuid/uuid.dart';
import 'dtos.dart';
import 'local-storage-service.dart';

Uuid uuid = new Uuid();
String USER_ID = uuid.v4();
const API_BASE_URL_TEST = "https://this-or-that-test.azurewebsites.net/this-or-that";
const API_BASE_URL_PROD = "https://this-or-that-api.azurewebsites.net/this-or-that";
const API_BASE_URL = API_BASE_URL_PROD;
const NOT_CLOSED_ERR = "NOT_CLOSED_ERR";

class ApiService {
  static Future<DecisionSet> fetchNewDecisionSet(String surveyId) async {
    String url = API_BASE_URL + "/" + surveyId + "/vote";
    final response = await http.get(url, headers: {
      "userId": USER_ID,
    });

    print("=>" + response.body);

    if (response.statusCode == 200) {
      var decisionSet = DecisionSet.fromJson(json.decode(response.body));
      LocalStorageService.saveParticipated(surveyId, decisionSet.perspective);
      return decisionSet;
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
      if (response.statusCode == 400) {
        throw Exception(NOT_CLOSED_ERR);
      } else {
        throw Exception("Error");
      }
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

  static Future<void> postImage(String code, String base64DataUri) async {
    var map = new Map<String, dynamic>();
    map["file"] = base64DataUri;
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
