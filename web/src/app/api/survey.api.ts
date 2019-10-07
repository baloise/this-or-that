import Vue from 'vue';
import {classToPlain, plainToClass} from 'class-transformer';
import {CreateSurveyRequest} from '@/app/models/create-survey-request';
import {CreateSurveyResponse} from '@/app/models/create-survey-response';
import {CreateImageRequest} from '@/app/models/create-image-request';
import {CreateImageResponse} from '@/app/models/create-image-response';

const BASE_PATH = '/api/survey';

export async function createSurvey(createSurveyRequest: CreateSurveyRequest): Promise<CreateSurveyResponse> {
    const response = await Vue.$http.post(BASE_PATH, classToPlain(createSurveyRequest));
    return plainToClass(CreateSurveyResponse, response.data);
}

export async function createImage(createImageRequest: CreateImageRequest, surveyCode: string): Promise<CreateImageRequest> {
    const response = await Vue.$http.post(BASE_PATH + '/' + surveyCode + '/image', classToPlain(createImageRequest));
    return plainToClass(CreateImageResponse, response.data);
}

export async function startSurvey(surveyCode: string): Promise<Response> {
    return await Vue.$http.post(BASE_PATH + '/' + surveyCode + '/start');
}
