<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::table('orders', function (Blueprint $table) {
            // Diisi penjual saat verifikasi/kemas
            $table->date('estimasi_siap')->nullable()->after('status');
            $table->date('estimasi_kirim')->nullable()->after('estimasi_siap');
        });
    }

    public function down(): void
    {
        Schema::table('orders', function (Blueprint $table) {
            $table->dropColumn(['estimasi_siap', 'estimasi_kirim']);
        });
    }
};
