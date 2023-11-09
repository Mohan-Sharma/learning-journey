def call(commerceDir, platformHome, projectName) {
	echo "##### Executing commerce build ##### -- ${platformHome}"
	def customCodeDir = commerceDir + '/' + projectName
	sh """
		#!/bin/bash
		cd ${platformHome} && . ./setantenv.sh
		cd ${customCodeDir}/core-customize/devops && ant develop && ant createdevdirs
	"""
	addProperty(commerceDir, "solrserver.instances.default.autostart=false")
	sh """cd ${platformHome} && . ./setantenv.sh && ant clean all"""
}
   