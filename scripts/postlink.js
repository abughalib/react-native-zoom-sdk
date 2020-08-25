'use strict'

const xcode = require('xcode');
const fs = require('fs');
const path = require('path');
const packageJson = require('../../package.json');

const frameworks = [
    'libsqlite3.tbd',
    'libstdc++.6.0.9.tbd',
    'libz.1.2.5.tbd',
    'VideoToolbox.framework',
];

const projectName = packageJson.name;
const projectPath = `./ios/${projectName}.xcodeproj/project.pbxproj`;
const xcodeProject = xcode.project(projectPath);
const pbxProject = xcodeProject.parseSync();
const target = pbxProject.getFirstTarget().uuid;

console.log('Adding Frameworks...');
frameworks.map(framework =>{
    pbxProject.addFramework(framework, {link: true, target});
    pbxProject.addStaticLibrary(framework, {
        target
    });
});

fs.writeFileSync(
    projectPath,
    pbxProject.writeSync()
);

const settingsGradlePath = './android/settings.gradle';
const settingsGradle = fs.readFileSync(settingsGradlePath, 'utf-8');
const projectDir = '../node_modules/react-native-zoom-sdk';

const getIncludeForProject = (name) => (
    `\ninclude ':${name}'\n` +
    `project(':${name}').projectDir = ` +
    `new File(rootProject.projectDir, '${path.join(projectDir, 'android', name)}')\n`
  );

const newSettingsGradle = projectsToInclude.reduce((content, project) =>
content.replace(/\n/, getIncludeForProject(project)), settingsGradle);


fs.writeFileSync(settingsGradlePath, newSettingsGradle);  