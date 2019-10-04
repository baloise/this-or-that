import Vue from 'vue';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCoffee, faUser, faTrophy, faEnvelope, faLink, faExclamationCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faUser, faTrophy, faEnvelope, faLink, faExclamationCircle);

Vue.component('font-awesome-icon', FontAwesomeIcon);
