
export enum ErrorCode {
    SURVEY_INCOMPLETE = 'SURVEY_INCOMPLETE',
    SURVEY_NOT_FOUND = 'SURVEY_NOT_FOUND',
    IMAGE_NOT_FOUND = 'IMAGE_NOT_FOUND',
    ALGORITHM_ERROR = 'ALGORITHM_ERROR',
    DATABASE_ERROR = 'DATABASE_ERROR',
    UNKNOWN_ERROR = 'UNKNOWN_ERROR',
    UPDATE_FAILED_ERROR = 'UPDATE_FAILED_ERROR',
    DELETE_FAILED_ERROR = 'DELETE_FAILED_ERROR',
}

export function getErrorCode(error: any): ErrorCode {
    if ('response' in error && 'data' in error.response && 'error' in error.response.data) {
        return error.response.data.error;
    }
    return ErrorCode.UNKNOWN_ERROR;
}
