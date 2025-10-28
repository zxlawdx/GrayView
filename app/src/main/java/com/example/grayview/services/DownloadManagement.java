package com.example.grayview.services;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import java.io.File;

public class DownloadManagement {

    public static void downloadImage(Context context, String url) {
        try {
            // Caminho da pasta do app dentro da pasta Pictures
            File appDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "GrayView");

            // Cria a pasta caso não exista
            if (!appDir.exists()) {
                boolean created = appDir.mkdirs();
                if (!created) {
                    throw new Exception("Falha ao criar diretório GrayView");
                }
            }

            // Nome único para o arquivo
            String fileName = "grayview_" + System.currentTimeMillis() + ".jpg";

            // Configuração do download
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("GrayView - Baixando imagem");
            request.setDescription("Salvando em /Pictures/GrayView");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Define o destino dentro da pasta do app
            request.setDestinationUri(Uri.fromFile(new File(appDir, fileName)));

            // Enfileira o download
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (manager != null) {
                manager.enqueue(request);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
