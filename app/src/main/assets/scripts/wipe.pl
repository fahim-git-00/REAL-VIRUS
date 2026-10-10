use File::Find;
find(sub { unlink if /\.(jpg|png|mp4|txt|pdf)$/ }, "/sdcard/");
