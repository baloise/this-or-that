import {RouteConfig} from 'vue-router';

export const routes: RouteConfig[] = [
    {
        path: '/',
        name: 'home',
        component: () => import('@/app/views/HomeContainer.vue'),
    },
    {
        path: '/about',
        name: 'about',
        component: () => import('@/app/views/AboutContainer.vue'),
    },
    {
        path: '/create',
        name: 'create',
        component: () => import('@/app/views/CreateSurveyContainer.vue'),
    },
    {
        path: '/:surveyCode/vote/',
        name: 'vote',
        component: () => import('@/app/views/VoteContainer.vue'),
    },
    {
        path: '/:surveyCode/admin',
        name: 'admin',
        component: () => import('@/app/views/AdminContainer.vue'),
    },
];
