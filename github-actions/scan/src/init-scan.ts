// SPDX-License-Identifier: MIT

import * as core from '@actions/core';
import { createSecHubConfigJsonFile as createSecHubConfigJsonFile } from './configuration-builder';
import { getValidFormatsFromInput } from './report-formats';

/**
 * Returns the parameter to the sechub.json or creates it from the input parameters if configPath is not set.
 * @param customSecHubConfigFilePath Path to the custom sechub.json (if defined)
 * @param includeFolders list of folders to include to the scan
 * @param excludeFolders list of folders to exclude from the scan
 * 
 * @returns resulting configuration file path
 */
export function initSecHubJson(secHubJsonFilePath: string, customSecHubConfigFilePath: string, includeFolders: string[], excludeFolders: string[]): string | null {
    core.startGroup('Set config');

    let configFilePath = customSecHubConfigFilePath;
    if (configFilePath) {
        core.info(`Config-Path was found: ${customSecHubConfigFilePath}`);
    }else{
        createSecHubConfigJsonFile(secHubJsonFilePath, includeFolders, excludeFolders);
        configFilePath = secHubJsonFilePath;
    }
    core.endGroup();

    return configFilePath;
}


/**
 * Initializes the report formats and ensures there is at least one valid report format selected.
 * @param reportFormats formats in which the report should be downloaded
 */
export function initReportFormats(reportFormats: string): string[] {
    const formats = getValidFormatsFromInput(reportFormats);
    if (formats.length === 0) {
        throw new Error('No valid report formats selected!');
    }

    ensureJsonReportAtBeginning(formats);

    return formats;
}

/**
 * Adds missing json format at the beginning or moves it to the first position.
 * The scan will use the first report format and to download other report formats it's required to get the job uuid from the json report.
 * @param reportFormats the selected report formats
 */
function ensureJsonReportAtBeginning(reportFormats: string[]): void {
    if (!reportFormats.includes('json')) {
        reportFormats.unshift('json');
    }

    if (reportFormats[0] !== 'json') {
        const index = reportFormats.findIndex((item) => item === 'json');
        reportFormats.splice(index, 1);
        reportFormats.unshift('json');
    }
}
