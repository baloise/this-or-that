import Vue from 'vue';
import {library} from '@fortawesome/fontawesome-svg-core';
import {faUpload, faUser, faTrophy, faEnvelope, faLink, faExclamationCircle, faInfoCircle} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome';

library.add(faUpload, faUser, faTrophy, faEnvelope, faLink, faExclamationCircle, faInfoCircle);

Vue.component('font-awesome-icon', FontAwesomeIcon);
