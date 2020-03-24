def depotManifest(depotNumber,contentRoot,localPath="*",depotPath=".",recursive="1",exclude="*.pdb"){
    def depot_manifest = libraryResource 'de/simonrenger/depot_build.vdf.tpl'
    depot_manifest.replaceAll('[STEAM_DEPOT_ID]',depotNumber)
    depot_manifest.replaceAll('[CONTENT_ROOT]',contentRoot)
    depot_manifest.replaceAll('[STEAM_LOCAL_PATH]',localPath)
    depot_manifest.replaceAll('[STEAM_DEPOT_PATH]',depotPath)
    depot_manifest.replaceAll('[STEAM_DEPOT_RECURSIVE]',recursive)
    depot_manifest.replaceAll('[STEAM_DEPOT_EXCLUDE]',exclude)
    writeFile file: "depot_build_${depotNumber}.vdf", text: depot_manifest
}

def appManifest(appId,depotNumber,outputdir,steamBranch,isPreview=0,contentroot=""){
    def app_manifest = libraryResource 'de/simonrenger/app_build.vdf.tpl'
    app_manifest.replaceAll('[STEAM_APP_ID]',appId)
    app_manifest.replaceAll('[JOB_BASE_NAME]',JOB_NAME)
    app_manifest.replaceAll('[BUILD_NUMBER]',BUILD_NUMBER)
    app_manifest.replaceAll('[STEAM_IS_PREVIEW_BUILD]',isPreview)
    app_manifest.replaceAll('[STEAM_BRANCH]',steamBranch)
    app_manifest.replaceAll('[OUTPUT_DIR]',outputdir)
    app_manifest.replaceAll('[CONTENT_ROOT]',contentroot)
    app_manifest.replaceAll('[STEAM_DEPO_NUMBER]',depotNumber)
    writeFile file: "app_build_${appId}.vdf", text: app_manifest
}

def deploy(credentials,appId){
    withCredentials([usernamePassword(credentialsId: credentials, passwordVariable: 'STEAMCMD_PASSWORD', usernameVariable: 'STEAMCMD_USERNAME')]) {
        bat label: '', script: "\"${STEAM_BUILDER}\" +login \"${STEAMCMD_USERNAME}\" \"${STEAMCMD_PASSWORD}\" +run_app_build_http \"app_build_${appId}.vdf\" +quit"
    }
}