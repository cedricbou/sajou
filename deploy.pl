#!/bin/perl

use strict;
use locale;
use warnings;

use LWP::UserAgent;
use Http::Request::Common;
#use HTTP::Request::StreamingUpload;
use File::Slurp;

use POSIX;
use constant DATETIME => strftime("%Y%m%d%H%M%S", localtime);

my $war = 'target/sajou-1.0.0-SNAPSHOT.war';
my $appName = 'sajou';
my $tomcatUser = 'admin';
my $tomcatPass = 'azqswx!';
my @tomcatServers = ('127.0.0.1:18081', '127.0.0.1:18082'); 


_deploy();

sub _deploy {
	for my $tomcatServer (@tomcatServers) {
		print "[deploy] upload war to $tomcatServer\n";
		upload_war_to_server($war, $tomcatServer, $appName);
		print "[deploy] upload done for $tomcatServer\n";
		my $deployedApps = list_app_on_server($tomcatServer, $appName);
		for my $deployedApp (@{$deployedApps}) {
			print "[deploy] found deployed app : $deployedApp\n";
		}
	}
}

sub _rollback {

}

sub upload_war_to_server {
	my ($war, $server, $appName) = @_;

	my $manager = build_manager_upload_url($server, $appName);

	my $data = read_file($war , binmode => ':raw' , scalar_ref => 1);

	my $ua = LWP::UserAgent->new;
#	$ua->timeout(120);

	my $req = HTTP::Request->new(
     	'PUT', $manager,
		HTTP::Headers->new(
          'Content-Type'   => 'application/x-zip',
      	),
      	$data
  	);

	$req->authorization_basic('admin', 'azqswx!');

	my $response = $ua->request( $req );

	print $response->content;
}

sub list_app_on_server {
	my ($server, $appName) = @_;

	my $ua = LWP::UserAgent->new;
	$ua->timeout(60);

	my $req = HTTP::Request->new(
     	'GET', build_manager_url($server) . '/list' );

	$req->authorization_basic('admin', 'azqswx!');

	my $response = $ua->request( $req );

	my @lines = split /\n/, $response->content;
	my @appLines = ();

	for my $line (@lines) {
		chomp $line;
		if($line =~ /^\/$appName\:/ ) {
			push @appLines, $line;
		}
	}

	return \@appLines;
}

sub get_last_version_from_apps {
	my ($apps) = @_;
}

sub build_manager_url {
	my ($server) = @_;

	return 'http://' . $server . '/manager/text';
}

sub build_manager_upload_url {
	my ($server, $appName) = @_;

	return build_manager_url($server) . '/deploy?path=/' . $appName . '&version=' . DATETIME . '&update=true';
}