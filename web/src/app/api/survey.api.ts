import Vue from 'vue';
import {classToPlain, plainToClass} from 'class-transformer';
import {CreateSurveyRequest} from '@/app/models/create-survey-request';
import {CreateSurveyResponse} from '@/app/models/create-survey-response';
import {CreateImageRequest} from '@/app/models/create-image-request';
import {CreateImageResponse} from '@/app/models/create-image-response';
import {VoteRequest} from '@/app/models/vote-request';
import {VoteResponse} from '@/app/models/vote-response';
import {ScoreResponse} from '@/app/models/score-response';

const BASE_PATH = '';

export async function createSurvey(createSurveyRequest: CreateSurveyRequest): Promise<CreateSurveyResponse> {
    const response = await Vue.$http.post(BASE_PATH + '/create', classToPlain(createSurveyRequest));
    return plainToClass(CreateSurveyResponse, response.data);
}

export async function createImage(createImageRequest: CreateImageRequest, surveyCode: string): Promise<CreateImageRequest> {
    const response = await Vue.$http.post(BASE_PATH + '/' + surveyCode + '/image', classToPlain(createImageRequest));
    return plainToClass(CreateImageResponse, response.data);
}

export function getImageURL(surveyCode: string, imageId: string): string {
    return BASE_PATH + '/' + surveyCode + '/image/' + imageId;
}

export async function getImage(surveyCode: string, imageId: string): Promise<string> {
    const response = await Vue.$http.get(BASE_PATH + '/' + surveyCode + '/image/' + imageId);
    return response.data;
}

export async function startSurvey(surveyCode: string): Promise<Response> {
    return await Vue.$http.post(BASE_PATH + '/' + surveyCode + '/start');
}

export async function getVote(surveyCode: string): Promise<VoteResponse> {
    const response = await Vue.$http.get(BASE_PATH + '/' + surveyCode + '/vote');
    return plainToClass(VoteResponse, response.data);
}

export async function setVote(surveyCode: string, voteRequest: VoteRequest): Promise<Response> {
    return await Vue.$http.post(BASE_PATH + '/' + surveyCode + '/vote', classToPlain(voteRequest));
}

export async function stopSurvey(surveyCode: string): Promise<Response> {
    return await Vue.$http.post(BASE_PATH + '/' + surveyCode + '/stop');
}

export async function getScore(surveyCode: string): Promise<ScoreResponse> {
    const response = await Vue.$http.get(BASE_PATH + '/' + surveyCode + '/score');
    return plainToClass(ScoreResponse, response.data);
}
