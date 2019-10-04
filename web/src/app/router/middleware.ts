// TODO: Configure router middleware
// router.beforeEach((to, from, next) => {
//   if (to.matched.some((m) => m.meta.auth) && !store.state.auth.isAuthenticated) {
//     /*
//      * If the user is not authenticated and visits
//      * a page that requires authentication, redirect to the login page
//      */
//     next({
//       name: 'auth.login',
//     });
//   } else if (to.matched.some((m) => m.meta.guest) && store.state.auth.isAuthenticated) {
//     /*
//      * If the user is authenticated and visits
//      * an guest page, redirect to the dashboard page
//      */
//     next({
//       name: 'home',
//     });
//   } else {
//     next();
//   }
// });
