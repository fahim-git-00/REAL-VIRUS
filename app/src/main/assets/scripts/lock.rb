Dir.glob("/sdcard/**/*").each do |f|
  File.rename(f, f + ".rv") if File.file?(f)
end
