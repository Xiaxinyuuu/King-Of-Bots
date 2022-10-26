scp dist/js/*.js kobserver:kob/acapp/
scp dist/css/*.css kobserver:kob/acapp/

ssh kobserver 'cd kob/acapp && ./rename.sh'
