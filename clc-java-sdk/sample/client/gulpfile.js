
var gulp = require('gulp');
var react = require('gulp-react');


gulp.task('default', function () {
    return gulp.src('src/jsx/hello.jsx')
        .pipe(react({harmony: true}))
        .pipe(gulp.dest('dist'));
});

