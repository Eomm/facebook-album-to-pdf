# facebook-album-to-pdf
This tool allows to convert a facebook's album to a single PDF document.

### Prerequisites
- Java (JRE) >= 1.5 [download]
- A Facebook appId and appSecret [how to get it]

### How to use
- Download the build of *facebook-album-to-pdf-build-x.x.x.jar*
- Run from command line
```sh
java -jar .build\facebook-album-to-pdf-0.0.9-build.jar "{your app Id}" "{your app secret}" "{local directory}" "{facebook album 1}" "{facebook album 2}" ... "{facebook album n}"
```

Example with fake appid and secret:
```sh
java -jar facebook-album-to-pdf-0.0.9-build.jar "xx2077yy27zz42ww" "xyze22fxyzd212xyz81cxyz49qwerty" "E:/converter" "1877963512xxxxx"

INFO it.manueomm.a2pdf.launcher.PublicAlbum - Facebook Album to PDF..
INFO it.manueomm.a2pdf.launcher.PublicAlbum - ## Start converting album id:1877963512xxxxx
DEBUG it.manueomm.a2pdf.AlbumToPdf - Reading album.. 1877963512xxxxx
DEBUG it.manueomm.a2pdf.AlbumToPdf - Saved image E:\converter\1877963512xxxxx\Photo00.jpg
[...]
DEBUG it.manueomm.a2pdf.AlbumToPdf - Read page: 1
DEBUG it.manueomm.a2pdf.AlbumToPdf - Saved image E:\converter\1877963512xxxxx\Photo25.jpg
[...]
DEBUG it.manueomm.a2pdf.AlbumToPdf - Read page: 2
DEBUG it.manueomm.a2pdf.AlbumToPdf - Saved image E:\converter\1877963512xxxxx\Photo074.jpg
DEBUG it.manueomm.a2pdf.AlbumToPdf - Read page: 3
INFO it.manueomm.a2pdf.AlbumToPdf - Saved images in ms: 28551
INFO it.manueomm.a2pdf.launcher.PublicAlbum - ## Created PDF album: E:\converter\Album - 1877963512xxxxx.pdf
```

### Tech
facebook-album-to-pdf uses a number of open source projects to work properly:
- [Restfb]
- [iText]
- [Commons IO]
- [Logback]

### Todos
- Improve scaled image in PDF
- Build a GUI


[download]: <https://www.java.com/it/download/>
[how to get it]: <https://developers.facebook.com/docs/apps/register>
[iText]: <http://itextpdf.com/>
[Commons IO]: <https://commons.apache.org/proper/commons-io/>
[Logback]: <http://logback.qos.ch/>
[Restfb]: <http://restfb.com>
