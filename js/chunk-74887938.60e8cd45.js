(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-74887938"],{"0635":function(t,e,s){},"1eea":function(t,e,s){},3894:function(t,e,s){"use strict";s.r(e);var i=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("section",{attrs:{id:"vote"}},[s("section",{staticClass:"hero is-info is-bold is-fullheight"},[s("div",{staticClass:"hero-head"},[s("nav",{staticClass:"navbar",attrs:{role:"navigation"}},[s("div",{staticClass:"navbar-brand"},[s("div",{staticClass:"navbar-item"},[s("button",{staticClass:"button is-info is-inverted is-outlined",on:{click:function(e){return t.back()}}},[t._v(" ⬅️ Back ")])]),s("div",{staticClass:"navbar-item",staticStyle:{flex:"1","white-space":"nowrap",overflow:"hidden","text-overflow":"ellipsis"}},[t.voteResponse?s("span",{staticClass:"title",staticStyle:{"margin-bottom":"0","white-space":"nowrap",overflow:"hidden","text-overflow":"ellipsis"}},[t._v(" "+t._s(t.voteResponse.perspective)+" "),s("small",[t._v("(Votes: "+t._s(t.submittedVotes)+" SurveyCode: "+t._s(this.$route.params.surveyCode)+")")])]):t._e()])])])]),s("div",{staticClass:"hero-body"},[s("div",{staticClass:"container has-text-centered"},[t.voteResponse&&null!=t.voteResponse.id1&&!t.isOver?s("div",[s("div",{staticClass:"image-container columns is-centered",class:t.isLandscape?"is-mobile":""},[s("div",{staticClass:"column is-half"},[s("VoteImage",{attrs:{src:t.getImageURL1()},nativeOn:{click:function(e){return t.vote(1)}}})],1),s("div",{staticClass:"column is-half"},[s("VoteImage",{attrs:{src:t.getImageURL2()},nativeOn:{click:function(e){return t.vote(2)}}})],1)])]):t._e(),t.isOver||t.voteResponse&&null==t.voteResponse.id1?s("div",{staticClass:"columns is-centered has-text-centered"},[s("div",{staticClass:"column"},[s("h1",{staticClass:"title"},[t._v("Its over ...")]),s("h1",{staticClass:"subtitle"},[t._v("... survey has already finished, but you can view the results")]),s("b-button",{attrs:{type:"is-info",size:"is-medium",inverted:""},on:{click:function(e){return t.manageSurvey()}}},[t._v(" View survey results ")])],1)]):t._e()])]),s("b-loading",{attrs:{active:t.isLoading,"can-cancel":!0,"is-full-page":!0},on:{"update:active":function(e){t.isLoading=e}}})],1)])},n=[],o=s("9ab4"),a=s("60a3"),r=s("c3df"),c=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"inner"},[s("figure",{staticClass:"image is-square"},[s("img",{attrs:{src:t.src,alt:"image to vote"}})])])},u=[],l=function(t){function e(){return null!==t&&t.apply(this,arguments)||this}return Object(o["d"])(e,t),Object(o["c"])([Object(a["b"])({default:""})],e.prototype,"src",void 0),e=Object(o["c"])([a["a"]],e),e}(a["d"]),d=l,v=d,p=(s("7a71"),s("2877")),h=Object(p["a"])(v,c,u,!1,null,"59dab978",null),b=h.exports,f=function(t){function e(){var e=null!==t&&t.apply(this,arguments)||this;return e.isLoading=!0,e.isOver=!1,e.isLandscape=!1,e.voteResponse=null,e.submittedVotes=0,e}return Object(o["d"])(e,t),e.prototype.getImageURL1=function(){return null!=this.voteResponse?Object(r["c"])(this.$route.params.surveyCode,this.voteResponse.id1):null},e.prototype.getImageURL2=function(){return null!=this.voteResponse?Object(r["c"])(this.$route.params.surveyCode,this.voteResponse.id2):null},e.prototype.mounted=function(){return Object(o["b"])(this,void 0,void 0,(function(){var t,e,s=this;return Object(o["e"])(this,(function(i){switch(i.label){case 0:this.isLoading=!0,this.isLandscape=window.innerHeight<window.innerWidth,window.addEventListener("resize",(function(){s.isLandscape=window.innerHeight<window.innerWidth}),!1),i.label=1;case 1:return i.trys.push([1,3,4,5]),t=this,[4,Object(r["e"])(this.$route.params.surveyCode)];case 2:return t.voteResponse=i.sent(),null!==localStorage.getItem("this-or-that:"+this.$route.params.surveyCode)&&(this.submittedVotes=parseInt(localStorage.getItem("this-or-that:"+this.$route.params.surveyCode),10)),[3,5];case 3:if(e=i.sent(),"response"in e&&"status"in e.response&&404===e.response.status)return this.$router.push("/404"),[2];if("response"in e&&"status"in e.response&&400===e.response.status)return this.isOver=!0,[2];throw e;case 4:return this.isLoading=!1,[7];case 5:return[2]}}))}))},e.prototype.vote=function(t){return Object(o["b"])(this,void 0,void 0,(function(){var e,s,i,n,a;return Object(o["e"])(this,(function(o){switch(o.label){case 0:this.isLoading=!0,o.label=1;case 1:return o.trys.push([1,5,6,7]),null==this.voteResponse?[3,4]:(e=1===t?this.voteResponse.id1:this.voteResponse.id2,s=1===t?this.voteResponse.id2:this.voteResponse.id1,i={winner:e,loser:s},[4,Object(r["f"])(this.$route.params.surveyCode,i)]);case 2:return o.sent(),n=this,[4,Object(r["e"])(this.$route.params.surveyCode)];case 3:n.voteResponse=o.sent(),this.submittedVotes+=1,localStorage.setItem("this-or-that:"+this.$route.params.surveyCode,this.submittedVotes.toString()),o.label=4;case 4:return[3,7];case 5:if(a=o.sent(),"response"in a&&"status"in a.response&&404===a.response.status)return this.$router.push("/404"),[2];if("response"in a&&"status"in a.response&&400===a.response.status)return this.isOver=!0,[2];throw a;case 6:return this.isLoading=!1,[7];case 7:return[2]}}))}))},e.prototype.manageSurvey=function(){this.$router.push("admin")},e.prototype.back=function(){this.$router.push("/")},e=Object(o["c"])([Object(a["a"])({components:{VoteImage:b}})],e),e}(a["d"]),m=f,g=m,w=(s("ed50"),Object(p["a"])(g,i,n,!1,null,"ad9fe5de",null));e["default"]=w.exports},"7a71":function(t,e,s){"use strict";s("1eea")},ed50:function(t,e,s){"use strict";s("0635")}}]);
//# sourceMappingURL=chunk-74887938.60e8cd45.js.map