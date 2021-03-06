load 'deploy'

load 'config/deploy' # remove this line to skip loading any of the default tasks

set :application, "sajou"

default_run_options[:pty] = true

# DEPLOYMENT SCHEME
set :scm, :none
set :deploy_via, :copy
set :repository do 
  fetch(:deploy_from)
end

# LOCAL
set :war, "./target/sajou-1.0.0-SNAPSHOT.war"

# TOMCAT SERVERS
# role :webserver, "testserver"
set :tomcat_home, "/usr/share/tomcat55/"
set :tomcat_ctrl, "/etc/init.d/tomcat55"

# USER / SHELL
set :user, "robotdeploy" # the user to run remote commands as
set :use_sudo, false

set :deploy_from do 
  dir = "c:/tmp/prep_#{release_name}"
  system("mkdir -p #{dir}")
  dir
end

# this is capistrano's default location.
# depending on the permissions of the server
# you may need to create it and chown it over
# to :user (e.g. chown -R robotuser:robotuser /u)
set :deploy_to do 
  "/u/apps/#{application}"
end

#
# simple interactions with the tomcat server
#
namespace :tomcat do

  desc "start tomcat"
  task :start do
    sudo "#{tomcat_ctrl} start"
  end

  desc "stop tomcat"
  task :stop do
    sudo "#{tomcat_ctrl} stop"
  end

  desc "stop and start tomcat"
  task :restart do
    tomcat.stop
    tomcat.start
  end

  desc "tail :tomcat_home/logs/*.log and logs/catalina.out"
  task :tail do
    stream "tail -f #{tomcat_home}/logs/*.log #{tomcat_home}/logs/catalina.out"
  end

end

#
# link the current/whatever.war into our webapps/whatever.war
#
after 'deploy:setup' do
  cmd = "ln -sf #{deploy_to}/current/`basename #{war}` #{tomcat_home}/webapps/`basename #{war}`"
  puts cmd
  sudo cmd
end

# collect up our war into the deploy_from folder
# notice that all we're doing is a copy here,
# so it is pretty easy to swap this out for
# a wget command, which makes sense if you're
# using a continuous integration server like
# bamboo. (more on this later).
before 'deploy:update_code' do
  unless(war.nil?)
    puts "get war cp #{war} #{deploy_from}"
    system("cp #{war} #{deploy_from}")
    puts system("ls -l #{deploy_from}")
  end
end

# restart tomcat
namespace :deploy do
  task :restart do
    tomcat.restart
  end
end

#
# Disable all the default tasks that
# either don't apply, or I haven't made work.
#
namespace :deploy do
  [ :upload, :cold, :start, :stop, :migrate, :migrations ].each do |default_task|
    desc "[internal] disabled"
    task default_task do
      # disabled
    end
  end

  namespace :web do
    [ :disable, :enable ].each do |default_task|
      desc "[internal] disabled"
      task default_task do
        # disabled
      end
    end
  end

  namespace :pending do
    [ :default, :diff ].each do |default_task|
      desc "[internal] disabled"
      task default_task do
        # disabled
      end
    end
  end
end