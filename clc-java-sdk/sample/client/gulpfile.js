
var gulp = require('gulp');
var react = require('gulp-react');
var source = require('vinyl-source-stream');
var browserify = require('browserify');
var watchify = require('watchify');
var reactify = require('reactify');
var streamify = require('gulp-streamify');


gulp.task('default', ['build']);

gulp.task('build', function () {
    browserify({
        entries: ['./src/jsx/app.jsx'],
        transform: [reactify]
    })
    .bundle()
    .pipe(source('app.js'))
    .pipe(gulp.dest('dist'))
    .pipe(gulp.dest('../app/src/main/resources/static'));
});

gulp.task('watch', function() {
    gulp.watch('src/jsx/**/*.jsx', ['build']);
});