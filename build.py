#!/usr/bin/python
# coding=utf-8
# encoding: utf-8
import requests
import subprocess
import os
modules = ['weight-realme']
CONSOLE_TEXT_PATH = "console.txt"
URL = "https://static-qa.lifesense.com/"
DIR = "app/src/main/assets/web/"




def runShell(cmd):
    print "start runShell:", cmd
    process = subprocess.Popen(cmd, shell=True)
    process.wait()
    return_code = process.returncode
    print "finish runShell: %s return_code %s" % (return_code, process.returncode)

def readConsoleText(path):
    file = open(path);
    for module in modules:
        while 1:
            lines = file.readlines(100000)
            if not lines:
                break
            for line in lines:
                sub_fix = os.path.splitext(line)[1]
                pre_fix = os.path.splitext(line)[0]
                reg = 'dist/'+module+"/";
                if sub_fix != '' and sub_fix != '.gz\n' and pre_fix.startswith(reg) :
                    path = line.replace(reg,'').replace('\n','')
                    assets_remote_url = URL + module + "/" + path;
                    assets_local_path = DIR + module + "/" + path;
                    runShell(
                        "sh download.sh"+ " " + assets_local_path+ " " + assets_remote_url)


def downloadWebRes(url):
    print "start download webapp res"
    for module in modules:
        response = requests.get(url + module + "/app.json");
        if response.status_code == 200:
            res = response.json();
            parseAppJson(module, res)


def parseAppJson(module, json):
    print "start parse web info"
    assets = json["assets"];
    assets_paths = assets.keys();
    for assets_path in assets_paths:
        assets_remote_url = URL + module + "/" + assets_path;
        assets_local_path = DIR + module + "/" + assets_path;
        runShell(
            "sh download.sh"+ " " + assets_local_path+ " " + assets_remote_url)
    entries = json["entries"];
    for entry in entries:
        entry_remote_url = URL + module + "/" + entry + ".html";
        entry_local_path = DIR + module + "/" + entry + ".html";
        runShell(
            "sh download.sh" + " " + entry_local_path + " " + entry_remote_url)


def main():
    print "startBuild"
    readConsoleText(CONSOLE_TEXT_PATH);
    print "startBuildApk"
    runShell("sh buildall.sh")
    print "finshBuildApk"


if __name__ == '__main__':
    main()
