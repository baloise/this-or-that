import {Score} from '@/app/models/score';

export class ScoreResponse {
    public scores!: Score[];
    public surveyIsRunning!: boolean;
    public perspective?: string;
    public numberOfVotes?: number;
    public numberOfUsers?: number;
}
