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
        path: '/howitworks',
        name: 'howitworks',
        component: () => import('@/app/views/HowItWorksContainer.vue'),
    },
    {
        path: '/404',
        component: () => import('@/app/views/NotFound.vue'),
    },
    {
        path: '/privacy',
        component: () => import('@/app/views/PrivacyContainer.vue'),
    },
    {
        path: '/tos',
        component: () => import('@/app/views/TermOfServiceContainer.vue'),
    },
    {
        path: '/:surveyCode/vote',
        name: 'vote',
        component: () => import('@/app/views/VoteContainer.vue'),
    },
    {
        path: '/:surveyCode/admin',
        name: 'admin',
        component: () => import('@/app/views/AdminContainer.vue'),
    },
    {
        path: '/:surveyCode',
        redirect: '/:surveyCode/vote',
    },
    {
        path: '*',
        redirect: '/404',
    },
];
