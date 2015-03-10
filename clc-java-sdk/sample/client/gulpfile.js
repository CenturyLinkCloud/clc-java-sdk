//!

var gulp = require('gulp');
var react = require('gulp-react');
var source = require('vinyl-source-stream');
var browserify = require('browserify');
var watchify = require('watchify');
var reactify = require('reactify');
var es6ify = require("es6ify")
var streamify = require('gulp-streamify');
var sass = require('gulp-sass');

function toDist() {
    return gulp.dest('dist')
}

function toAppStatic() {
    return gulp.dest('../app/src/main/resources/static');
}

gulp.task('buildJs', function () {
    browserify()
        .add(es6ify.runtime)
        .transform(reactify)
        .transform(es6ify.configure(/.jsx/))
        .require(require.resolve('./src/jsx/app.jsx'), { entry: true })
        .on('error', function(err) {
            console.log(err.message);
        })
        .bundle()
        .pipe(source('app.js'))
        .pipe(toDist())
        .pipe(toAppStatic());
});

gulp.task('buildCss', function () {
    gulp.src('src/scss/sample.scss')
        .pipe(sass())
        .pipe(toDist())
        .pipe(toAppStatic());
});

gulp.task('buildHtml', function () {
    gulp.src('src/index.html')
        .pipe(toDist())
        .pipe(toAppStatic());
});

gulp.task('default', ['buildJs', 'buildCss', 'buildHtml']);

gulp.task('watch', function() {
    gulp.watch('src/jsx/**/*.jsx', ['buildJs']);
    gulp.watch('src/scss/**/*.scss', ['buildCss']);
    gulp.watch('src/index.html', ['buildHtml']);
});