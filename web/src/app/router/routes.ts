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
        path: '/admin/:surveyCode',
        name: 'admin',
        component: () => import('@/app/views/AdminContainer.vue'),
    },
];
