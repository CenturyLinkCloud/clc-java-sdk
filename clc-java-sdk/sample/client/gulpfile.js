//!

var gulp = require('gulp');
var react = require('gulp-react');
var source = require('vinyl-source-stream');
var browserify = require('browserify');
var watchify = require('watchify');
var reactify = require('reactify');
var streamify = require('gulp-streamify');
var sass = require('gulp-sass');


gulp.task('buildJs', function () {
    browserify({
        entries: ['./src/jsx/app.jsx'],
        transform: [reactify]
    })
    .bundle()
    .pipe(source('app.js'))
    .pipe(gulp.dest('dist'))
    .pipe(gulp.dest('../app/src/main/resources/static'));
});

gulp.task('buildCss', function () {
    gulp.src('src/scss/sample.scss')
        .pipe(sass())
        .pipe(gulp.dest('dist'))
        .pipe(gulp.dest('../app/src/main/resources/static'));
});

gulp.task('buildHtml', function () {
    gulp.src('src/index.html')
        .pipe(gulp.dest('dist'))
        .pipe(gulp.dest('../app/src/main/resources/static'));
});

gulp.task('default', ['buildJs', 'buildCss', 'buildHtml']);

gulp.task('watch', function() {
    gulp.watch('src/jsx/**/*.jsx', ['buildJs']);
    gulp.watch('src/scss/**/*.scss', ['buildCss']);
    gulp.watch('src/index.html', ['buildHtml']);
});